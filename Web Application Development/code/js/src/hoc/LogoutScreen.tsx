import React from 'react'
import { useHistory } from 'react-router-dom'

import * as UserSession from '../login/UserSession'
import { useContext } from 'react'

import Button from '@material-ui/core/Button';

import '../css/Center.css';

type PageProps = {
    redirectPath: string
}

export function LogoutScreen({ redirectPath }: PageProps) {
    const history = useHistory();
    const userSession = useContext(UserSession.Context)

    function handleLogout() {

        userSession?.logout();
        history.push(redirectPath)
    }

    return (
        <div className="screen">
            <Button variant="contained"
                onClick={() => handleLogout()}
            >
                Terminar Sessão
            </Button>
        </div>
    )
}
