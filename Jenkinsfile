pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'git-credentials', branch: 'sarra-dev', 
                    url: 'https://github.com/kenza-20/Devops-projet.git'
            }
        }

        stage('Clean Workspace') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Build Project') {
            steps {
                sh 'mvn package'
            }
        }
    }
    
    post {
        failure {
            echo 'Build failed! Check logs for errors.'
        }
        success {
            echo 'Build completed successfully.'
        }
    }
}
