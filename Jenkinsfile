pipeline {
    agent {
        dockerfile {
            dir 'build'
            args """
                  -v /etc/passwd:/etc/passwd
                  -v /etc/group:/etc/group
                  -v ${JENKINS_HOME}/.m2:${JENKINS_HOME}/.m2
                  -v ${JENKINS_HOME}/.ssh:${JENKINS_HOME}/.ssh
                  """
        }
    }
    options {
        ansiColor 'xterm'
        buildDiscarder(logRotator(numToKeepStr: '30'))
    }
    stages {
        stage('Compile') {
            steps {
                sh './mvnw -B -Dstyle.color=always -Djansi.force=true clean compile'
            }
        }
        stage('Install') {
            steps {
                sh './mvnw -B -Dstyle.color=always -Djansi.force=true verify'
            }
            post {
                always {
                    junit 'target/**/*.xml'
                }
            }
        }
        stage('Promotion') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    input 'Release to artifact repository?'
                }
            }
        }
        stage('Release') {
            steps {
                sh "./mvnw -B -Dstyle.color=always -Djansi.force=true release:clean release:prepare release:perform"
            }
        }
    }
}
