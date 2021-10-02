import React from 'react'
import Button from '@material-ui/core/Button';
import { useHistory, useParams, BrowserRouter as Router } from "react-router-dom";

import '../css/Center.css';
import { NextStateList } from '../components/NextState';

import { ParamTypes } from '../models'

export function NextStateScreen() {
    const history = useHistory()

    let { pid } = useParams<ParamTypes>();
    return (
        <div>
            <div className="screen">
                <div className="box-shadow">
                    <NextStateList />
                </div>
            </div>
            <div className="screen">
                <Button variant="contained" onClick={() => history.push(`/project/${pid}`)}>Go back</Button>
            </div>
        </div>

    )
}
