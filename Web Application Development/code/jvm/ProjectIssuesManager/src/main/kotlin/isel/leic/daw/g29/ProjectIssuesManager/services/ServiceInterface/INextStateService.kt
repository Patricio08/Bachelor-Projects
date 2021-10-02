package isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.NextStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.models.NextState

interface INextStateService {
    fun getNextStates(projectName: String, stateName: String): List<NextState>
    fun postNextState(nextState: NextStateInputModel): String
}