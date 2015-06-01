package co.limoges.hq

import scala.collection._

class Topic(val name:String) {
	var events = mutable.ArrayBuffer[String]()

	def write(event:String) {
		events += event
	}

	def read(consumer:Consumer) : String = {
		events(consumer.bookmarked(name))
	}
}

case class Client(val id:String)
class Consumer(val id:String) {
	var bookmarks = Map[String, Int]()

	def bookmarked(topic:String) : Int = {
		bookmarks.getOrElse(topic, 0)
	}
}

case class Transaction(topic:String, event:String, operation:String)
class Broker {
	var topics = mutable.Map[String, Topic]()
	var sessions = 0
	var consumers = mutable.Map[String, Consumer]()

	def view() = topics.keys

	def write(topic:String, event:String) : Transaction = {
		if (!topics.contains(topic)) {
			topics(topic) = new Topic(topic)
		}
		topics(topic).write(event)
		new Transaction(topic, event, "add")
	}

	// TODO This is absolutely not a proper session ID but with limited time and inexperience
	// with scala and scalatra, this will have to do to identify clients.
	def generateSessionId() : String = {
		// Simply hand the current number of users as key and increment afterward
		// We keep a table of our consumer bookmarks
		val sessionId:String = this.sessions.toString
		sessionId
	}

	def read(topic:String, sessionId:String) : Option[String] = {
		// Check if the topic exists
		if (!topics.contains(topic)) {
			return Option(null)
		}
		// Check if the consumer has an existing session id
		if (!consumers.contains(sessionId)) {
			// Re-use if necessary
			consumers += (sessionId -> new Consumer(sessionId))
		}
		Option(topics(topic).read(consumers(sessionId)))
	}
}


