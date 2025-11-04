pipeline {
    agent any

    tools {
        // Must match names in "Global Tool Configuration"
        jdk 'jdk-21'
        maven 'maven-3.9'
    }

    environment {
        // Your json-server base URL (started manually on port 3000)
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
                // If your tests read System.getProperty("baseUrl"), use this:
                // bat 'mvn test -DbaseUrl=%BASE_URL%'
                bat 'mvn test'
            }
        }

        stage('Publish Reports') {
            steps {
                echo 'Publishing Cucumber reports...'

                // Cucumber Reports plugin:
                // Make sure your runner generates: json:target/cucumber-report.json
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
        }
    }
}
