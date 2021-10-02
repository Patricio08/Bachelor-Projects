import React from 'react'
import { useHistory } from 'react-router-dom'

import * as UserSession from '../login/UserSession'
import { useContext } from 'react'

import Button from '@material-ui/core/Button';

import '../css/Center.css';

type PageProps = {
    redirectPath: string
}

export function InvalidCredentialsScreen({ redirectPath }: PageProps) {
    const history = useHistory();
    const userSession = useContext(UserSession.Context)

    function handleBack() {
        userSession?.logout();
        history.push(redirectPath)
    }

    return (
        <div className="screen">
            <Button variant="contained"
                onClick={() => handleBack()}
            >
                Go Back to Login
            </Button>
        </div>
    )
}
