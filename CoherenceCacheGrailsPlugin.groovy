
import grails.plugin.springcache.DefaultCacheResolver
import grails.plugin.springcache.taglib.CachingTagLibDecorator
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.slf4j.LoggerFactory
import org.springframework.web.filter.DelegatingFilterProxy
import grails.plugin.springcache.aop.*
import grails.plugin.springcache.web.*

class CoherenceCacheGrailsPlugin {

	def version = "1.3.2-SNAPSHOT"
	def grailsVersion = "1.2.0 > *"
	def dependsOn = [:]
	def pluginExcludes = [
			"grails-app/views/**",
			"web-app/**",
			"**/.gitignore",
			"grails-app/*/grails/plugin/springcache/test/**",
	]
	def observe = ["groovyPages"]
	def loadAfter = ["groovyPages"]
	
	def author = "Grails Plugin Collective"
	def authorEmail = "grails.plugin.collective@gmail.com"
	def title = "Coherence Cache Plugin"
	def description = "Provides annotation-driven caching of service methods and page fragments."
	def documentation = "http://gpc.github.com/grails-springcache"

	def doWithWebDescriptor = {xml ->
		if (isEnabled(application)) {
			def filters = xml.filter
			def lastFilter = filters[filters.size() - 1]
			lastFilter + {
				filter {
					"filter-name" "springcacheContentCache"
					"filter-class" DelegatingFilterProxy.name
					"init-param" {
						"param-name" "targetBeanName"
					}
					"init-param" {
						"param-name" "targetFilterLifecycle"
						"param-value" "true"
					}
				}
			}

			def filterMappings = xml."filter-mapping"
			def lastMapping = filterMappings[filterMappings.size() - 1]
			lastMapping + {
				"filter-mapping" {
					"filter-name" "springcacheContentCache"
					"url-pattern" "*.dispatch"
					dispatcher "FORWARD"
					dispatcher "INCLUDE"
				}
			}
		}
	}

	def doWithSpring = {
		if (!isEnabled(application)) {
			log.warn "Springcache plugin is disabled"
		} else {
			if (application.config.grails.spring.disable.aspectj.autoweaving) {
				log.warn "Service method caching is not compatible with the config setting 'grails.spring.disable.aspectj.autoweaving = false'"
			}

			springcacheCachingAspect(CachingAspect) {
				springcacheService = ref("coherenceCacheService")
			}

			springcacheFlushingAspect(FlushingAspect) {
				springcacheService = ref("coherenceCacheService")
			}

			springcacheDefaultCacheResolver(DefaultCacheResolver)
			//springcacheDefaultKeyGenerator(DefaultKeyGenerator)
		}
	}

	def doWithDynamicMethods = {ctx ->
	}

        /*
	def doWithApplicationContext = { applicationContext ->
		def decorator = new CachingTagLibDecorator(applicationContext.springcacheService)
		for (tagLibClass in application.tagLibClasses) {
			decorator.decorate(tagLibClass, applicationContext."${tagLibClass.fullName}")
		}
	}

	def onChange = { event ->
		if (application.isTagLibClass(event.source)) {
			def tagLibClass = application.getTagLibClass(event.source.name)
			def instance = event.ctx."${event.source.name}"
			def decorator = new CachingTagLibDecorator(event.ctx.springcacheService)
			
			decorator.decorate(tagLibClass, instance)
		}
	}
        */
	private static final log = LoggerFactory.getLogger("grails.plugin.coherencecache.CoherenceCacheGrailsPlugin")

	private boolean isEnabled(GrailsApplication application) {
		application.config.with {
			(springcache.enabled == null || springcache.enabled != false) && !springcache.disabled 
		}
	}

}
