package isel.leic.daw.g29.ProjectIssuesManager.controllers

import isel.leic.daw.g29.ProjectIssuesManager.controllers.inputModel.StateInputModel
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren.SirenEntity
import isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.StatesOutputModel
import isel.leic.daw.g29.ProjectIssuesManager.services.ServiceInterface.IStateServices
import org.springframework.web.bind.annotation.*

@RestController
class StateController(private val stateServices: IStateServices) {

    @GetMapping(STATE_PART)
    fun getState(): SirenEntity<StatesOutputModel> {
        val states = stateServices.getStates()
                .map { State -> State.mapToOutputModel() }
        return StatesOutputModel(states.size).mapToSiren(states)
    }

    @PostMapping(STATE_PART, "application/json")
    fun postState(
            @RequestBody state: StateInputModel
    ) = stateServices.postState(state)

    @DeleteMapping(STATE_PART)
    fun deleteState(
            @RequestBody state: StateInputModel
    ) = stateServices.deleteState(state)

}