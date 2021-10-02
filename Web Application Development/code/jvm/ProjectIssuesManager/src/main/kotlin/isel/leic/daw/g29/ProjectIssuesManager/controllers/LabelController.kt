package isel.leic.daw.g29.ProjectIssuesManager.controllers

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.LabelInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.LabelsOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.ILabelService
import org.springframework.web.bind.annotation.*

@RestController
class LabelController(private val labelService: ILabelService) {

    @GetMapping(LABEL_PART)
    fun getLabels(): SirenEntity<LabelsOutputModel> {
        val labels = labelService.getLabels()
                .map { Label -> Label.mapToOutputModel() }
        return LabelsOutputModel(labels.size).mapToSiren(labels)
    }

    @PostMapping(LABEL_PART, "application/json")
    fun postLabel(
            @RequestBody label: LabelInputModel
    ) = labelService.postLabel(label)

    @DeleteMapping(LABEL_PART, "application/json")
    fun deleteLabel(
            @RequestBody label: LabelInputModel
    ) = labelService.deleteLabel(label)
}