package grails.plugin.template.cache

import grails.plugin.coherencecache.CoherenceCacheService;

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

class TemplatecacheTagLib {
  CoherenceCacheService coherenceCacheService

  static namespace ='cache';

  static private Log log = LogFactory.getLog(TemplatecacheTagLib)

  //<cache:render .../>
  def render={attrs, body ->
    cachingContent(attrs, body, {a, b -> g.render(a)})
  }

  //<cache:cache>any contents </cache:cache>
  def cache={attrs, body ->
    cachingContent(attrs, body, {a, b -> b()})
  }

  private cachingContent(attrs, body, renderClozure){
    if (!coherenceCacheService) {
      coherenceCacheService  = applicationContext.coherenceCacheService
    }
    //[org.hibernate.cache.UpdateTimestampsCache, Book, org.hibernate.cache.StandardQueryCache]
    def id=attrs.template?:''
    if (attrs.cachekey)
      id+=attrs.remove("cachekey")

      log.debug("$id caching ?")
      if(attrs.cachedisabled && attrs.cachedisabled.toBoolean() || !coherenceCacheService.isEnabled()){
        log.debug("$id caching disabled (${!coherenceCacheService.isEnabled()})")
        attrs.remove('cachedisabled')
        out<<renderClozure(attrs, body)
        return
      }
    def cachename=attrs.remove("cachename")?:'GSPCACHE';
    def cache = coherenceCacheService.getCache(cachename);
    log.debug(cache)

    if(cache == null){
      log.warn("$id: no cache [$cachename] ")
      out <<renderClozure(attrs, body)
      return
    }

    def element = cache.get(id);
    if(!element){
      log.debug("$id: not in cache")
      def r=renderClozure(attrs, body)
      log.debug("$id: completed rendering")
      element=r
      if(!(r.contains("Exception:") && r.contains('.gsp:'))){
        log.debug("$id: storing")
        cache.put(id, element);
      }else{
        log.debug("$i not put in cache\n$r")
      }

      log.debug("$id: cache miss")
    }else{
      log.debug("$id: cache hit")
    }
    log.debug("$id: start output")
    out << element
    log.debug("$id: end output")
  }


}

