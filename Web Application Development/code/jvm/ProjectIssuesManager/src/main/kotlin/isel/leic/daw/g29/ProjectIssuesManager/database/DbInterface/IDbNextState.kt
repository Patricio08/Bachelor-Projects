package isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.NextStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.NextState

interface IDbNextState {
    fun getNextStates(projectName: String, stateName: String): List<NextState>
    fun postNextState(nextState: NextStateInputModel): String
}