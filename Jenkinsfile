pipeline {
    agent any

    tools {
        // Use whatever names you configured in Jenkins (Global Tool Configuration)
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        // If you need any base URL for your API
        BASE_URL = 'http://localhost:3000'   // change to your real API URL
    }

    stages {

        stage('Checkout') {
            steps {
                // Pull code from the same Git repo where this Jenkinsfile lives
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Compile and download dependencies, but skip tests here
                bat 'mvn clean compile -DskipTests'
            }
        }

        // OPTIONAL: Only if your API / JSON server must be started on Jenkins machine
        // If your API is running on a different server (QA env, dev env), you can skip this.
        /*
        stage('Start API Server') {
            steps {
                // example: starting JSON-server in another window
                // Adjust command to whatever you actually use
                bat 'start cmd /c json-server --watch db.json --port 3000'
            }
        }
        */

        stage('Run API Tests') {
            steps {
                // You can add tags if you want only some tests
                // e.g. mvn test -Dcucumber.filter.tags="@RegressionTest"
                bat 'mvn test'
            }
        }

        stage('Publish Reports') {
            steps {
                // 1) Publish JUnit style reports from surefire
                junit 'target/surefire-reports/*.xml'

                // 2) Publish Cucumber HTML/JSON reports (if plugin installed)
                // After you install "Cucumber Reports" plugin in Jenkins:
                // cucumber buildStatus: 'UNSTABLE',
                //          fileIncludePattern: 'target/cucumber-report.json',
                //          trendsLimit: 10
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished. Cleaning up...'
            // Here you could also kill API server if you started one earlier
        }
    }
}
