import ListItem from '@material-ui/core/ListItem';
import TextField from '@material-ui/core/TextField';
import React, { useEffect } from 'react'

import * as Siren from '../Siren'
import Button from '@material-ui/core/Button';
import { usePostFetch } from '../services/usePostFetch'


export function PostAction({ action, responseField }: { action: Siren.Action, responseField: React.Dispatch<React.SetStateAction<string>> }) {
    const [postBody, setpostBody] = React.useState(null);
    const [errorState, setErrorState] = React.useState('');
    const PostNumber = usePostFetch('http://localhost:8081/api' + action.href, postBody)
    useEffect(() => { 
        if (PostNumber.type == 'response' && PostNumber.status == 200) {
            setErrorState('')
            responseField(PostNumber.body)
        }
        if (PostNumber.type == 'response' && PostNumber.status != 200){
            setErrorState(JSON.parse(PostNumber.body).detail)
        }
    })

    function handlePostRequest() {
        let body: any = {};
        action.fields.forEach(element => {
            body[element.name] = (document.getElementById(`${element.name} ${action.method}`) as HTMLInputElement).value
        });
        setpostBody(body)
    }

    let actionInput = <div>
        {
            action.fields.map((field) => {
                if (field.type == "text") {
                    return (
                        <div key={field.name}>
                            <TextField className="standard-basic" id={field.name + " " + action.method} label={field.name} />

                        </div>
                    )
                }
            })
        }

        <Button variant="contained"
            onClick={() => handlePostRequest()}>Submit
        </Button> 
        {errorState != '' ? 
            <div className="errorBox">
                {errorState}
            </div>
            :
            <></>
        }
        

    </div>


    return (
        <ListItem >
            {actionInput}
        </ListItem>

    )
}