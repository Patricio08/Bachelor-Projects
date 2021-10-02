package isel.leic.daw.g29.ProjectIssuesManager.controllers.outputModel.Siren

interface IOutputModelList<T,S> {
    fun mapToSiren(list: List<S>): SirenEntity<T>
}