package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren

interface IOutputModel<T> {
    fun mapToSiren(): SirenEntity<T>
}