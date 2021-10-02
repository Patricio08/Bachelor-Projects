import React from 'react'
import {
    useHistory,
} from "react-router-dom";

import { StateList } from '../components/States'
import Button from '@material-ui/core/Button';
import '../css/Center.css';

export function StateScreen() {
    const history = useHistory();
    return (
        <div>
            <div className="screen">
                <div className="box-shadow">
                    <StateList />
                </div>
            </div>
            <div className="screen">
                <Button variant="contained" onClick={() => history.push('/')}>Go back</Button>
            </div>
        </div>
    )
}
