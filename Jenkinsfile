pipeline {
    environment {
	    registry = 'glazaror/mkdocs'
	    registryCredential = 'docker-creds'
	    dockerImage = ''
	}

	agent any
	
	stages {
		stage ('Compile Stage') {
			steps {
				withMaven(maven: 'maven_3_6_3') {
					sh 'mvn clean compile'
				}
			}
		}
		
		stage ('Testing Stage') {
			steps {
				withMaven(maven: 'maven_3_6_3') {
					sh 'mvn test'
				}
			}
		}
		
		stage ('Deployment Stage') {
			steps {
				withMaven(maven: 'maven_3_6_3') {
					sh 'mvn install'
				}
			}
		}

		stage ('Publish Stage') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'glazaror-dockerhub') {
                        def customImage = docker.build('glazaror/cliente-microservice')
                        customImage.push()
                    }
                }
            }
        }

        stage ('Delivery Stage') {
            steps {
                script {
			def remote = [:]
			remote.name = "microservices-glazaror-test-server"
			remote.host = "microservices-glazaror-test-server.southcentralus.cloudapp.azure.com"
			remote.allowAnyHosts = true
			withCredentials([usernamePassword(credentialsId: 'glazaror-azure-ssh', passwordVariable: 'password', usernameVariable: 'userName')]) {
				remote.user = userName
        			remote.password = password
				
				//writeFile file: 'test.sh', text: 'ls'
				try {
			    		sshCommand remote: remote, command: 'docker stop cliente-microservice && docker rm cliente-microservice && docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'
					echo 'cliente-microservice was running and it was stopped'
				} catch (Exception e) {
					echo 'starting cliente-microservice'
					sshCommand remote: remote, command: 'docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'
				}
    			}			
		}
            }
        }

	}
	
	post {
		always {
			cleanWs()
		}
	}
}
