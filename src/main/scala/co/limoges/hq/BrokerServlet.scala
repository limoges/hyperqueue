package co.limoges.hq

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._
import scala.collection._
import scalate.ScalateSupport

class BrokerServlet extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {

	// Use our implementation of a broker to power the servlet
	var broker = new Broker;

	// Use automatic JSON serialization of case classes
	protected implicit val jsonFormats: Formats = DefaultFormats

	// The service deals entirely in the JSON format, contentType
	// should therefore reflect that.
	before() {
		contentType = formats("json")
	}

	get("/") {
		broker.view()
	}

	// Consumer must retrieve their sessionId before starting to consume topics
	post("/:topic") {
		broker.write(params("topic"), params("event"))
	}

	get("/:topic") {
		broker.read(params("topic"), params("sessionId")) match {
			case Some(e)	=> Ok(e);
			case None	=> NotFound(params("topic") + " not found");
				
		}
	}
}

