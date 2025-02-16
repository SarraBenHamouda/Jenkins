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
                            credentialsId: 'git-credentials'  // ✅ Ensure correct Jenkins credentials ID
                        ]]
                    ])
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE} .'  // ✅ Tagging correctly
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
                script {
                    // Stop and remove the existing container safely
                    sh """
                    if docker ps -aq -f name=my-nginx-container | grep -q .; then
                        docker stop my-nginx-container || true
                        docker rm my-nginx-container || true
                    fi
                    """

                    // Run the new container with auto-restart
                    sh 'docker run -d --restart=always -p 8081:80 --name my-nginx-container ${DOCKER_IMAGE}'
                }
            }
        }
    }
}
