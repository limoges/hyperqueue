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

class Consumer(val id:String) {
	var bookmarks = Map[String, Int]()

	def bookmarked(topic:String) : Int = {
		bookmarks.getOrElse(topic, 0)
	}
}

case class Transaction(topic:String, event:String, operation:String)
class Broker {
	var topics = mutable.Map[String, Topic]()
	var subs = mutable.Map[String, Consumer]()

	def view() = topics.keys

	def write(topic:String, event:String) : Transaction = {
		if (!topics.contains(topic)) {
			topics(topic) = new Topic(topic)
		}
		topics(topic).write(event)
		new Transaction(topic, event, "add")
	}
}
