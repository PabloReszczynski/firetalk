(defproject firetalk "0.0.1-Snapshot"
  :description "FIXME: write description"
  :url "https://github.com/PabloReszczynski/firetalk"
  :license {:name "MIT"
            :url  "https://mit-license.org/"}
  :dependencies [[com.google.cloud/google-cloud-firestore "2.2.5"]
                 [datascript "1.0.7"]
                 [org.clojure/java.data "1.0.86"]]
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.0"]]}}
  :deploy-repositories [["releases" :clojars]]
  :aliases {"update-readme-version" ["shell" "sed" "-i" "s/\\\\[firetalk \"[0-9.]*\"\\\\]/[firetalk \"${:version}\"]/" "README.md"]}
  :release-tasks [["shell" "git" "diff" "--exit-code"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["changelog" "release"]
                  ["update-readme-version"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy"]
                  ["vcs" "push"]])
