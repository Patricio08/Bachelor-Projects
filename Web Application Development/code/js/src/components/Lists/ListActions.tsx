import List from '@material-ui/core/List';
import { PostAction } from '../PostAction'
import { DeleteAction } from '../DeleteAction'
import { PutAction } from '../PutAction'

import * as Siren from '../../Siren'
import React from 'react'

export function ListActions( { actions, responseField}: {actions : Siren.Action[], responseField: React.Dispatch<React.SetStateAction<string>>}) {
    return (
        <List component="nav" aria-label="contacts">
            {actions.map((act) => {
                
                if(act.method == "POST")
                    return <PostAction action={act} responseField={responseField} key={act.name}/>

                else if(act.method == "DELETE")
                    return <DeleteAction action={act} responseField={responseField} key={act.name}/>

                else if(act.method == "PUT")
                    return <PutAction action={act} responseField={responseField} key={act.name}/>
            })}
            
        </List>
    )
}