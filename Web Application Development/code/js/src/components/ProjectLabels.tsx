import React from 'react'
import {
    BrowserRouter as Router,
    useHistory,
    useParams
} from "react-router-dom";

import { useContext, useState } from 'react';
import * as UserSession from '../login/UserSession'

import { useFetch2 } from '../services/useFetch2';
import * as Siren from '../Siren'
import { projectLabelsDto, projectLabelDto } from '../models'
import { ListProjectLabelItem } from './Lists/ListProjectLabelItem';
import { ListActions } from './Lists/ListActions';
import { PathsContext } from '../App'

import { ParamTypes } from '../models'

export function ProjectLabelList({ }) {
    const [pendingProjectLabel, setPendingProjectLabel] = useState('');

    let { pid } = useParams<ParamTypes>();

    const pathsContexts = useContext(PathsContext)

    let uri = ''
    if(pathsContexts.fetch){
        if(pendingProjectLabel == "waiting")
        setPendingProjectLabel('')
        const path = pathsContexts.path.projectlabel_PATH.replace("{name}", pid)

        uri = `http://localhost:8081/api${path}`
    }

    const fetchProjectLabel = useFetch2(uri, pendingProjectLabel)

    

    let toret = <h3>Waiting response</h3>
    let actions = <p></p>

    if (fetchProjectLabel.type == 'response' && fetchProjectLabel.status === 200) { 
        const body: Siren.Entity<projectLabelsDto, projectLabelDto> = fetchProjectLabel.type == 'response' ? JSON.parse(fetchProjectLabel.body) : ''
        
        if (body.entities.length == 0) {
            toret = <p>There is no project labels</p>
        } else {
            toret = <ListProjectLabelItem entities={body.entities} />
        }
        if (body.actions != null)
            actions = <ListActions actions={body.actions} responseField={setPendingProjectLabel} />
    }

    if (fetchProjectLabel.type == 'response' && fetchProjectLabel.status != 200) { 
        toret = <p> {fetchProjectLabel.body} </p>  
    }

    return (

        <div>
            {toret}
            {actions}
        </div>

    )
}