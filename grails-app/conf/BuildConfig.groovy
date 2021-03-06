grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
	inherits("global") {
		excludes "xml-apis"
	}
	log "warn"
	repositories {
		grailsHome()
		grailsPlugins()
		grailsCentral()
		mavenLocal()
		mavenRepo "http://m2repo.spockframework.org/ext/"
		mavenRepo "http://m2repo.spockframework.org/snapshots/"
		mavenCentral()
	}
	dependencies {
		test("org.objenesis:objenesis:1.2") {
			exported = false
		}
	}
	plugins {
		test(":spock:0.5-groovy-1.7") {
			export = false
		}
		build(":release:1.0.0.RC3") {
			export = false
		}
	}
}
