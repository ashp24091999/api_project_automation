pipeline {
  agent any

  tools {
    jdk   'jdk-21'
    maven 'maven-3.9'   // must match Manage Jenkins > Tools > Maven name
  }

  environment {
    // Your API base URL (json-server you start manually)
    BASE_URL = 'http://localhost:3000'
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test API') {
      steps {
        echo "Running API tests against ${env.BASE_URL}"

        // If your tests read System.getProperty("baseUrl"), this will pass it:
        // Make sure your test code uses it, e.g. System.getProperty("baseUrl", "http://localhost:3000")
        bat 'mvn -B -DbaseUrl=%BASE_URL% clean test'
      }
    }

    stage('Publish Reports') {
      steps {
        echo 'Publishing Cucumber API reports...'

        // 1) JUnit results (if any). allowEmptyResults so it doesn't fail the build if none.
        junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true

        // 2) Find & publish the Cucumber HTML report (similar style to your previous project)
        script {
          // Adjust these paths to how your API project writes the Cucumber HTML
          // With @CucumberOptions(plugin = {"pretty","json:target/cucumber-report.json","html:target/cucumber-html-report"})
          // the main file is usually: target/cucumber-html-report/index.html
          def candidates = [
            'target/cucumber-html-report/index.html',
            'target/cucumber-html-report/cucumber.html',
            'target/cucumber-reports/index.html',
            'target/cucumber-reports/overview-features.html'
          ]

          def found = candidates.find { fileExists(it) }
          if (found) {
            echo "Publishing Cucumber report: ${found}"
            def dir  = found.substring(0, found.lastIndexOf('/'))
            def file = found.substring(found.lastIndexOf('/') + 1)
            publishHTML([
              reportDir: dir,
              reportFiles: file,
              reportName: 'Cucumber API Report',
              keepAll: true,
              alwaysLinkToLastBuild: true,
              allowMissing: false
            ])
          } else {
            echo "No Cucumber HTML report found in: ${candidates}"
          }
        }

        // 3) (Optional) keep raw artifacts too
        archiveArtifacts artifacts: 'target/**', fingerprint: true
      }
    }
  }

  post {
    always {
      echo 'Pipeline finished.'
    }
  }
}
