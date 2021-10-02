import React from 'react'
import Button from '@material-ui/core/Button';
import { useHistory, BrowserRouter as Router } from "react-router-dom";

import '../css/Center.css';
import { LabelList } from '../components/Label';

export function LabelsScreen() {
    const history = useHistory();
    return (
        <div className="screen">
            <div>
                <div className="screen">
                    <div className="box-shadow">
                        <LabelList />
                    </div>
                </div>
                <div className="screen">
                    <Button variant="contained" onClick={() => history.push(`/`)}>Go back</Button>
                </div>
            </div>
        </div>
    )
}
