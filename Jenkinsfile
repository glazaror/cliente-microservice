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
                    try {
                        sh 'docker stop cliente-microservice && docker rm cliente-microservice && docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'
                        echo 'cliente-microservice was running and it was stopped'
                    } catch (Exception e) {
                        echo 'starting cliente-microservice'
                        sh 'docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'
                    }
                }
            }
        }
sh 'docker top cliente-microservice || docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'

	}
	
	post {
		always {
			cleanWs()
		}
	}
}