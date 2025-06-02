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
                sh './mvnw jib:build'
            }
        }
    }
}
