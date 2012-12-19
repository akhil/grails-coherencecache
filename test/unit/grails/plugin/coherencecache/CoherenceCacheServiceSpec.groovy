/*
 * Copyright 2010 Rob Fletcher
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

import grails.plugin.spock.UnitSpec
import grails.spring.BeanBuilder
import spock.lang.Unroll



import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

class CoherenceCacheServiceSpec extends UnitSpec {

	NamedCache cache1, cache2
	CoherenceCacheService service

	def setup() {
		cache1 = CacheFactory.getCache("cache1")
		cache2 = CacheFactory.getCache("cache2")

		mockLogging CoherenceCacheService, true
		service = new CoherenceCacheService()
	}
}


class DeliberateException extends RuntimeException {
	DeliberateException() {
		super("thrown to test exception handling")
	}
}
