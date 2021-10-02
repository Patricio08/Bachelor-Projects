import React, { useRef, useState, useContext } from 'react'
import { usePostFetch2 } from '../services/usePostFetch2';
import FormControl from '@material-ui/core/FormControl';
import Button from '@material-ui/core/Button';
import { useHistory } from 'react-router';
import { PathsContext } from '../App'

type PageProps = {
    redirectPath: string
}

export function RegisterBox({ redirectPath }: PageProps) {

    const history = useHistory()
    const inputName = useRef(null)
    const inputPassword = useRef(null)

    const [body, setBody] = useState(null)


    const handleRegister = () => {
        const nameInput = inputName.current.value
        const passwordInput = inputPassword.current.value

        setBody({
            "name": nameInput,
            "password": passwordInput,
        })
    }

    const pathsContexts = useContext(PathsContext)

    const postCollab = usePostFetch2(`http://localhost:8081/api/collaborator`, body)
    if (postCollab.type == 'response') {
        history.push(redirectPath)
    }

    return (
        <div>
            <h3>Register</h3>
            <FormControl >
                <div>
                    <input type="name" name="name" placeholder="Your username" ref={inputName} />

                </div>
                <div>
                    <input type="password" name="password" placeholder="Your password" ref={inputPassword} />
                </div>
            </FormControl>
            <div>
                <Button variant="contained"
                    onClick={() => handleRegister()}>Submit
                </Button>
            </div>

        </div>
    )
}
