import React, {useContext} from 'react'
import {
    BrowserRouter as Router,
    useHistory,
    useParams
} from "react-router-dom";
import Button from '@material-ui/core/Button';

import { useFetch2 } from '../services/useFetch2'
import * as Siren from '../Siren'
import { projectDto } from '../models'

import { ParamTypes } from '../models'
import { PathsContext } from '../App'

export function ProjectByIdFetch({ }) {
    const history = useHistory();
    let { pid } = useParams<ParamTypes>();

    const pathsContexts = useContext(PathsContext)

    let uri = ''
    if(pathsContexts.fetch){
        
        const path = pathsContexts.path.project_PATH.replace("{name}", pid)

        uri = `http://localhost:8081/api${path}`
    }

    
    

    const fetchState = useFetch2(uri, "donthavestate")

    const status = fetchState.type == 'response' ? fetchState.status : '?'

    const state = fetchState.type

    let toret = <h3>Waiting response</h3>

    if (fetchState.type == 'response') {
        if (fetchState.status == 200) {
            const body: Siren.SubEntity<projectDto> = fetchState.type == 'response' ? JSON.parse(fetchState.body) : ''
            toret = <div>
                <p>{body.properties.name}</p>
                <p>{body.properties.description}</p>
                <p>{body.properties.startState}</p>
                <Button variant="contained"
                    onClick={() => history.push(body.links[1].href)}>Go to issue</Button>
                <Button variant="contained"
                    onClick={() => history.push(`/project/${pid}/projectState`)}>Check Project States</Button>
                <Button variant="contained"
                    onClick={() => history.push(`/project/${pid}/projectlabel`)}>Check Project Labels</Button>
            </div>
        } else {
            toret = <p> Invalid project </p>
        }
    }

    return (
        <div>
            <div>
                <p>URI: {uri}</p>

                <p>{status}</p>

                <p>{state}</p>

                {toret}
            </div>
        </div>
    )
}