pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './mvnw clean install'
            }
        }
        
        stage('Build Container') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh './mvnw jib:build -Djib.to.auth.username=${DOCKER_USERNAME} -Djib.to.auth.password=${DOCKER_PASSWORD}'
                }
            }
        }
    }
}
