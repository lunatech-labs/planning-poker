package com.github.lavrov.poker

case class PlanningSession(
    participants: List[Participant],
    estimates: Estimates
)

case class Participant(
    id: String,
    name: String
)

case class UserStory(
    description: String
)

case class Estimates(
    userStory: UserStory,
    participantEstimates: Map[String, Card]
)

sealed trait Card
object Card {
  case class Number(value: Int) extends Card
}

sealed trait Action
object Action {
  case class AddParticipant(participant: Participant) extends Action
  case class RegisterEstimate(participantId: String, card: Card) extends Action
}

object PlanningSession {
  import Action._
  def update(model: PlanningSession, action: Action): PlanningSession = action match {
    case AddParticipant(participant) =>
      model.copy(
        participants = participant :: model.participants)
    case RegisterEstimate(participantId, card) =>
      if (model.participants.exists(_.id == participantId))
        model.copy(
          estimates = model.estimates.copy(
            participantEstimates = model.estimates.participantEstimates.updated(participantId, card)))
      else
        model
  }
}