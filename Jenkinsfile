pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "sarra7/my-nginx:latest"
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'sarra-dev']], 
                        userRemoteConfigs: [[
                            url: 'https://github.com/kenza-20/Devops-projet.git',
                            credentialsId: 'git-credentials'  // ✅ Use correct ID from Jenkins credentials
                        ]]
                    ])
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE} .'  // ✅ Directly tag the correct image
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withDockerRegistry([credentialsId: 'docker-hub-credentials', url: '']) {
                    sh 'docker push ${DOCKER_IMAGE}'
                }
            }
        }

        stage('Deploy Container') {
            steps {
                sh 'docker rm -f my-nginx-container || true'
                sh 'docker run -d -p 8081:80 --name my-nginx-container ${DOCKER_IMAGE}'  // ✅ Changed port to avoid conflict
            }
        }
    }
}
