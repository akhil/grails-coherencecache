grails-coherencecache
=====================

put the coherence jar in the app lib

springcache compatible with oracle coherence

heavily inspired by grails springcache plugin

Currently @Cacheable and @CacheFlush work.

For GSP template caching following 2 are supported
<cache : cache>
<cache : render>

Further reading:
http://alexandre-masselot.blogspot.com/2011/09/grails-template-caching-system.html
Refer for using cache tags. EHCache and report related info not applicable.

http://www.oracle.com/technetwork/middleware/coherence/downloads/index.html
http://coherence.oracle.com/display/EXAMPLES/Home

Note: This plugin has no feature to configure the coherence cache. It is not needed as all configuratio can be passed during runtime as java arguments.
More at http://docs.oracle.com/cd/E18686_01/coh.37/e18677/gs_config.htm

