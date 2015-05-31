package co.limoges.hq

import org.scalatra._
import scalate.ScalateSupport

class BrokerServlet extends HyperqueueStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

}
