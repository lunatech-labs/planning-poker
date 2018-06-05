package com.github.lavrov.poker

import akka.actor.{Actor, ActorLogging, ActorRef, Props}


object SessionActor {
  final case class Subscribe(ref: ActorRef)
  final case class SessionAction(action: PlanningSession.Action)
  def props: Props = Props[SessionActor](new SessionActor)
}

class SessionActor extends Actor with ActorLogging {
  import SessionActor._

  var subscribers: Set[ActorRef] = Set.empty
  var planningSession: PlanningSession = PlanningSession(
    Set.empty,
    Set.empty,
    Estimates(
      UserStory("story description here"),
      Map.empty
    )
  )

  def receive: Receive = {
    case Subscribe(ref) =>
      log.info(s"New subscriber")
      subscribers += ref
      ref ! planningSession
    case SessionAction(action) =>
      val updated = PlanningSession.update(planningSession, action)
      planningSession = updated
      subscribers.foreach(_ ! planningSession)
  }
}