import React from 'react'
import { useFetch2 } from '../services/useFetch2'
import * as Siren from '../Siren'
import { projectDto, projectsDto } from '../models'
import { ListProjectsItem } from './Lists/ListProjectsItem';
import { ListActions } from './Lists/ListActions';

import * as UserSession from '../login/UserSession'
import {useContext} from 'react'
import { PathsContext } from '../App'
import '../css/Error.css'

export function ProjectList() {

    const [pendingProject, setPendingProject] = React.useState('waiting');
    const pathsContexts = useContext(PathsContext)
    let uri = ''
    if(pathsContexts.fetch){
        console.log("aqui")
        if(pendingProject == "waiting")
        setPendingProject('')
        

        uri = `http://localhost:8081/api${pathsContexts.path.projects_PATH}`
    }
    const fetchProjects = useFetch2(uri, pendingProject)

    console.log(fetchProjects)
    console.log(uri)
    console.log(pendingProject)
    let toret = <h3>Waiting response</h3>
    let actions = <p></p>
    if (fetchProjects.type == 'response' && fetchProjects.status === 200) { 
        const body: Siren.Entity<projectsDto, projectDto> = fetchProjects.type == 'response' ? JSON.parse(fetchProjects.body) : ''

        if (body.entities.length == 0) {
            toret = <p>There is no projects</p>
        } else {
            toret = <ListProjectsItem entities={body.entities} />
	        
        }
        if(body.actions != null)
	        actions = <ListActions actions={body.actions} responseField={setPendingProject} />
    }
    
    if (fetchProjects.type == 'response' && fetchProjects.status != 200) { 
        toret = <div className="errorBox">
                <p> {fetchProjects.body} </p> 
            </div> 
    }

    return (
        <div>
			<div>
	            {toret}
	            {actions}
			</div>
        </div>       
    )
}
