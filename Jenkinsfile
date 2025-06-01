pipeline {
    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-17'
            args '-v $HOME/.m2:/root/.m2'
        }
    }
    
    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=/root/.m2/repository'
    }

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
