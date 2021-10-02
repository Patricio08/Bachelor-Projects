package isel.leic.daw.g29.ProjectIssuesManager.services

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.LabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.database.DbInterface.IDbLabel
import isel.leic.daw.g29.ProjectIssuesManager.models.Label
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ILabelService
import org.springframework.stereotype.Service

@Service
class LabelServices(private val DbLabel: IDbLabel): ILabelService {
    override fun getLabels(): List<Label> = DbLabel.getLabels()

    override fun postLabel(input: LabelInputModel): String = DbLabel.postLabel(input)

    override fun deleteLabel(input: LabelInputModel): String = DbLabel.deleteLabel(input)

}