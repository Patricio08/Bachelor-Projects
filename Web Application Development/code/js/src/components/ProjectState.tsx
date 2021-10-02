import React from 'react'
import {
    BrowserRouter as Router,
    useParams
} from "react-router-dom";

import { useContext, useState } from 'react';

import { useFetch2 } from '../services/useFetch2';
import * as Siren from '../Siren'
import { projectStatesDto, projectStateDto } from '../models'
import { ListProjectStateItem } from './Lists/ListProjectStateItem';
import { ListActions } from './Lists/ListActions';
import { PathsContext } from '../App'

import { ParamTypes } from '../models'

export function ProjectStateList({ }) {
    const [pendingProjectState, setPendingProjectState] = useState('waiting');

    let { pid } = useParams<ParamTypes>();

    const pathsContexts = useContext(PathsContext)
    let uri = ''
    if(pathsContexts.fetch){
        if(pendingProjectState == "waiting")
        setPendingProjectState('')
        const path = pathsContexts.path.projectstate_PATH.replace("{name}", pid)

        uri = `http://localhost:8081/api${path}`
    }

    
    const fetchProjectState = useFetch2(uri, pendingProjectState)

    

    let toret = <h3>Waiting response</h3>
    let actions = <p></p>

    if (fetchProjectState.type == 'response' && fetchProjectState.status === 200) { 
        const body: Siren.Entity<projectStatesDto, projectStateDto> = fetchProjectState.type == 'response' ? JSON.parse(fetchProjectState.body) : ''
        if (body.entities.length == 0) {
            toret = <p>There is no project states</p>
        } else {
            toret = <ListProjectStateItem entities={body.entities} />
        }
        if (body.actions != null)
            actions = <ListActions actions={body.actions} responseField={setPendingProjectState} />
    }

    if (fetchProjectState.type == 'response' && fetchProjectState.status != 200) { 
        toret = <p> {fetchProjectState.body} </p>  
    }

    return (

        <div>
            {toret}
            {actions}
        </div>

    )
}