    select
        tbl.sequence_next_hi_value
    from
        hibernate_sequences tbl
    where
        tbl.sequence_name=? for update


    update
        hibernate_sequences
    set
        sequence_next_hi_value=?
    where
        sequence_next_hi_value=?
        and sequence_name=?

