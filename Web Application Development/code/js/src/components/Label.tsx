import React, { useContext } from 'react'

import { ListActions } from './Lists/ListActions';

import { useFetch2 } from '../services/useFetch2'
import * as Siren from '../Siren'
import { issueLabelsDto, issueLabelDto } from '../models'
import { ListLabelItem } from './Lists/ListLabelsItem';
import { PathsContext } from '../App'

export function LabelList() {

    const [pendingLabel, setPendingLabel] = React.useState('waiting');
    const pathsContexts = useContext(PathsContext)

    let uri = ''
    if(pathsContexts.fetch){
        if(pendingLabel == "waiting")
        setPendingLabel('')
        
        uri = `http://localhost:8081/api${pathsContexts.path.label_PATH}`
    }

    const fetchState = useFetch2(uri, pendingLabel)
    
    let toret = <h3>Waiting response</h3>

    let actions = <p></p>
    if (fetchState.type == 'response' && fetchState.status == 200) {
        const body: Siren.Entity<issueLabelsDto, issueLabelDto> = fetchState.type == 'response' ? JSON.parse(fetchState.body) : ''
        if (body.entities.length == 0) {
            toret = <p>There is no labels</p>
        } else {
            toret = <ListLabelItem entities={body.entities} />
                 
        }
        
        if(body.actions != null) {
            actions = <ListActions actions={body.actions} responseField={setPendingLabel} />
        } 

    }
    if (fetchState.type == 'response' && fetchState.status != 200) { 
        toret = <p> {fetchState.body} </p>  
    }

    return (
        <div>
            {toret}
            {actions}
        </div>
    )
}