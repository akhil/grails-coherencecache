package grails.plugin.template.cache

import grails.test.*

class TemplatecacheTagLibTests extends GroovyTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
      def s1 = "${new Date()}"
      def out1 = new TemplatecacheTagLib().cache([key:"123"], s1)
      Thread.sleep(2000)
      def s2 = "${new Date()}"
      def out2 = new TemplatecacheTagLib().cache([key:"123"], s2)
      println(out1)
      println(out2)
      assert s1 != s2
      assertEquals out1, out2 
    }
}
