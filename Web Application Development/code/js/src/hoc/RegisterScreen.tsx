import React from 'react'

import { RegisterBox } from '../components/RegisterBox'

import '../css/Center.css';

type PageProps = {
    redirectPath: string
}

export function RegisterScreen({ redirectPath }: PageProps) {

    return (
        <div className="screen">
            <RegisterBox redirectPath={redirectPath} />
        </div>
    )
}
