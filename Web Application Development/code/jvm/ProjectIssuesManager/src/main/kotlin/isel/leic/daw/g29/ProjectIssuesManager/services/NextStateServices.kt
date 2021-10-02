package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.LabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.NextStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbLabel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbNextState
import isel.leic.daw.g29.ProjectIssuesManager.database.DbLabel
import isel.leic.daw.g29.ProjectIssuesManager.exceptions.DomainNotFoundException
import isel.leic.daw.g29.ProjectIssuesManager.models.Label
import isel.leic.daw.g29.ProjectIssuesManager.models.NextState
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ILabelService
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.INextStateService
import org.springframework.stereotype.Service

@Service
class NextStateScervices(private val DbNextState: IDbNextState): INextStateService {

    override fun getNextStates(projectName: String, stateName: String): List<NextState> = DbNextState.getNextStates(projectName, stateName)

    override fun postNextState(nextStateInputModel: NextStateInputModel): String = DbNextState.postNextState(nextStateInputModel)

}