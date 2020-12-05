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


	}
	
	post {
		always {
			cleanWs()
		}
	}
}