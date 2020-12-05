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
                    withCredentials([file(credentialsId: 'aws-connection-pem', variable: 'connection-pem')]) {
                       sh "cp \$connection-pem /var/jenkins_home/aws-connection-pem.pem"
                       try {
                           sh 'ssh -i /var/jenkins_home/aws-connection-pem.pem ec2-user@ec2-3-17-162-0.us-east-2.compute.amazonaws.com && docker stop cliente-microservice && docker rm cliente-microservice && docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'
                           echo 'cliente-microservice was running and it was stopped'
                       } catch (Exception e) {
                           echo 'starting cliente-microservice'
                           sh 'ssh -i /var/jenkins_home/aws-connection-pem.pem ec2-user@ec2-3-17-162-0.us-east-2.compute.amazonaws.com && docker run -p 8090:8090 -d --name cliente-microservice glazaror/cliente-microservice'
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