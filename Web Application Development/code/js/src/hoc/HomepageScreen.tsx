import React from 'react'
import '../css/Center.css';
import '../css/Home.css';
import iselLogo from '../assets/isel.png'

export function HomepageScreen() {

    return (
        <div className="screen">
            <div>
                <div className="screen">
                    <a href="https://www.isel.pt/"><img src={iselLogo}></img></a>
                    <div className="text-center">
                        <span>Project made by:</span>
                        <h4>Fábio Vilela</h4>
                        <h4>Daniel Patrício</h4>
                        <h4>Tiago Fernandes</h4>

                    </div>
                </div>


                <div className="container">
                    <p> This is a work carried out by the three members of group 29 on class LI61D in 2021v semester within the scope of the DAW
                        course of Instituto Superior de Engenharia de Lisboa.
                        For more information visit our project:
                    </p>
                    <div className="screen">
                        <a href="https://github.com/isel-leic-daw/daw-project-li61d-g29">https://github.com/isel-leic-daw/daw-project-li61d-g29</a>
                    </div>

                </div>
            </div>


        </div>
    )
}
/*
<h4> Diogo Nogueira <br> Fábio Vilela <br> Miguel Paixão</h4>
                <br>
                <br>

<h6> This is a work carried out by the three members of group 11 on class LI51N in 1920v semester within the scope of the PI
                course of Instituto Superior de Engenharia de Lisboa. <br>
                For more information visit our project <a href=""></a>
                </h6>
                <a href="https://isel.pt/%22%3E">
                    <img src="https://www.isel.pt/media/assets/default/images/logo-isel.png" alt="ISEL" title="ISEL">
                </a>
*/