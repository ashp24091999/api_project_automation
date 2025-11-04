pipeline {
    agent any

    tools {
        jdk 'jdk-21'
        maven 'maven-3.9'
    }

    environment {
        BASE_URL = 'http://localhost:3000'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub...'
                checkout scm
            }
        }

        stage('Build Project') {
            steps {
                echo 'Compiling project and downloading dependencies...'
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Run API Tests') {
            steps {
                echo 'Running Cucumber + Rest Assured tests...'
                // if your code reads System.getProperty("baseUrl"), you can do:
                // bat 'mvn test -DbaseUrl=%BASE_URL%'
                bat 'mvn test'
            }
        }

        stage('Publish Reports') {
            steps {
                echo 'Publishing JUnit and Cucumber reports...'
                junit 'target/surefire-reports/*.xml'

                cucumber buildStatus: 'UNSTABLE',
                          fileIncludePattern: 'cucumber-report.json',
                          jsonReportDirectory: 'target',
                          sortingMethod: 'ALPHABETICAL',
                          trendsLimit: 10
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
            // no taskkill here; json-server is manual
        }
    }
}
