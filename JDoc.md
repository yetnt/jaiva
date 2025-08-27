# Jaiva Comment Documentation (JDoc)

Basically like JavaDoc. Essentially

For actual highlighting, There is a VSCode extension which make use of this (fixing this soon. As of yet it does not work)

To create a document comment in jaiva use `@*` instead of `@`

```jaiva
@* Documentation comment
@* fr fr
{
    This is just a basic comment, not a doc comment.
}
```

## Styling

`{inline code}`
`**bold**`
`_underline_`

## Tags

A tag has the following syntax:
```
@* tagName $> tag properties and or description
```

If your tag description is too long and you hate seeing it in one line, on the next omit the tagName and it will be added to the previous!

```
@* tagName $> This is a very long property which I
@*         $> would like to continue on the same line.
```

### Parameter

For defining parameters for functions