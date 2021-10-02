import React from 'react'
import { useHistory } from 'react-router-dom'
import '../css/Center.css';
import Button from '@material-ui/core/Button';

import notfound from '../assets/notfound.png'

export function NoMatchScreen() {
    const history = useHistory()
    return (
        <div className="screen">
            <div>
                <div>
                    <img src={notfound} />
                </div>
                <div className="screen">
                    <Button variant="contained"
                        onClick={() => history.push('/')}>
                        Go back home
                    </Button>
                </div>
            </div>
        </div>
    )
}