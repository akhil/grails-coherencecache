/*
 * Copyright 2012 Akhil Kodali
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.coherencecache

import grails.spring.BeanBuilder
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.slf4j.LoggerFactory
import org.springframework.context.*

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

class CoherenceCacheService implements ApplicationContextAware {

	static private final log = LoggerFactory.getLogger(CoherenceCacheService.class)
	static transactional = false

	ApplicationContext applicationContext
	//CacheManager springcacheCacheManager
	boolean autoCreateCaches = true // TODO: config?

	/**
	 * Flushes the specified cache or set of caches.
	 * @param cacheNamePatterns can be a single cache name or a regex pattern or a Collection/array of them.
	 */
	void flush(cacheNames) {
	  if (cacheNames instanceof String) cacheNames = [cacheNames]
          cacheNames.each { flushNamedCache(it) }
        }

        private void flushNamedCache(String cacheName) {
          CacheFactory.getCache(cacheName).clear()
        }

	/**
	 * Calls a closure conditionally depending on whether a cache entry from a previous invocation exists. If the
	 * closure is called its return value is written to the cache..
	 * @param cacheName The name of the cache to use.
	 * @param key The key used to get and put cache entries.
	 * @param closure The closure to invoke if no cache entry exists already.
	 * @return The cached value if a cache entry exists or the return value of the closure otherwise.
	 */
	def doWithCache(String cacheName, Serializable key, Closure closure) {
                NamedCache cache = CacheFactory.getCache(cacheName)
		return doWithCacheInternal(cache, key, closure)
	}

	private doWithCacheInternal(NamedCache cache, Serializable key, Closure closure) {
		def value = cache.get(key)
		if (!value) {
			if (log.isDebugEnabled()) log.debug "Cache '$cache.cacheName' missed with key '$key'"
			value = closure()
			cache.put(key, value)
		} else {
			if (log.isDebugEnabled()) log.debug "Cache '$cache.cacheName' hit with key '$key'"
		}
		return value
	}

}

class NoSuchCacheException extends RuntimeException {
	NoSuchCacheException(String cacheName) {
		super("No cache named '$cacheName' exists".toString())
	}
}
