package isel.leic.daw.g29.ProjectIssuesManager.controllers


import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.NextStateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.NextStatesOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.services.NextStateScervices
import org.springframework.web.bind.annotation.*

@RestController
class NextStateController(private val nextStateScervices: NextStateScervices) {

    @GetMapping(NEXTSTATE_PART)
    fun getNextState(
            @PathVariable name: String,
            @PathVariable stateName: String
    ): SirenEntity<NextStatesOutputModel> {
        println("aquiii")
        val NextState = nextStateScervices.getNextStates(name, stateName)
                .map { nextState -> nextState.mapToOutputModel() }
        return NextStatesOutputModel(name, stateName, NextState.size).mapToSiren(NextState)
    }

    @PostMapping(NEXTSTATE_PART, "application/json")
    fun postNextState(
            @RequestBody nextStateInputModel: NextStateInputModel,
            @PathVariable name: String,
            @PathVariable stateName: String
    ): String {
        nextStateInputModel.projectName = name
        nextStateInputModel.stateName = stateName
        return nextStateScervices.postNextState(nextStateInputModel)
    }


}