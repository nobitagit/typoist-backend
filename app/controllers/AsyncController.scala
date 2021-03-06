package controllers

import javax.inject._

import akka.actor.ActorSystem
import play.api.libs.json._
import play.api.mvc._

import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

import models.{Stock, Paragraph, ParagraphSeq}

//case class Paragraph(
//  text: String,
//)
//
//object Paragraph {
//  implicit val ParagraphWrites = new Writes[Paragraph] {
//    def writes(p: Paragraph) = Json.obj(
//      "text" -> p.text
//    )
//  }
//}

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param cc standard controller components
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.  When rendering content, you should use Play's
 * default execution context, which is dependency injected.  If you are
 * using blocking operations, such as database or network access, then you should
 * use a different custom execution context that has a thread pool configured for
 * a blocking API.
 */
@Singleton
class AsyncController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  private val paragraphs: List[String] = List(
    "One",
    "Two"
  )

  private val textCollection: Seq[Seq[String]] = Seq(
    Seq("I have been typing for 4 minutes", "This is a new sentence"),
    Seq("A new string in a Seq", "Typing involves exercising muscle memory")
  )
  /**
   * Creates an Action that returns a plain text message after a delay
   * of 1 second.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/message`.
   */
  def message = Action.async {
    getFutureMessage(1.second).map { msg => Ok(msg) }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success("Hi!")
    }(actorSystem.dispatcher) // run scheduled tasks using the actor system's dispatcher
    promise.future
  }

  def text: Action[AnyContent] = Action async {
    getRandomText().map {
      msg => Ok(msg)
    }
  }

  def getPar = Action {
    val stock = Paragraph("GOOG")
    Ok(Json.toJson(stock))
  }

  def getStock = Action {
    val stock = Stock("GOOG", 650.0)
    val ret: JsValue = Json.toJson(stock)
    Ok(ret)
  }


  def getArr = Action {
    Ok(Json.toJson(getRandomCollection))
  }

  private def getRandomCollection: JsValue = {
    val rand = new Random()
    val idx = rand.nextInt(textCollection.length)
    val ret = textCollection(idx)
    Json.toJson(ParagraphSeq(ret))
  }

  private def getRandomText(): Future[JsValue] = {
    val rand = new Random()
    val promise: Promise[JsValue] = Promise[JsValue]()

    actorSystem.scheduler.scheduleOnce(1.second) {
      val len = paragraphs.length
      val idx = rand.nextInt(len)
      val res = Paragraph(paragraphs(idx))

      promise.success(Json.toJson(res))
    }(actorSystem.dispatcher)
    promise.future
  }
}
