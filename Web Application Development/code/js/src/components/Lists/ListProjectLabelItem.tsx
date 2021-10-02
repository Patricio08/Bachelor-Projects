import React from 'react'
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import * as Siren from '../../Siren'
import { projectLabelsDto, projectLabelDto } from '../../models'


export function ListProjectLabelItem({ entities }: Siren.Entity<projectLabelsDto, projectLabelDto>) {

    return (
        <List component="nav" aria-label="contacts">
            {entities.map((ent) => 
                <ListItem 
                    key={ent.properties.labelName}
                    >
                    <ListItemText inset primary={ent.properties.labelName} />
                </ListItem>)}
        </List>
    )
}
